--Load in crime data
crime_data = LOAD '/home/fanxia/Documents/Study/Semester1/Hadoop/HW/HW5/data/crimes.csv' using PigStorage(',') 
	AS (id:chararray, case_number:chararray, date:chararray, block:chararray, primary_type:chararray, description:chararray, location_description:chararray, arrest:boolean, domestic:boolean, beat:chararray, district:chararray, ward:chararray, community_area:chararray, latitude:double, longitude:double);

--Filter out the header
crime_data = FILTER crime_data BY arrest is not null;
crime_data = FOREACH crime_data GENERATE id, primary_type, GetYear(ToDate(date, 'MM/dd/yyyy hh:mm:ss a', 'UTC')) AS year;

--Group the data using primary type and year
grouped_data = GROUP crime_data BY (primary_type, year);

--Flatten the grouped data and give names to attributes
counted_data = FOREACH grouped_data GENERATE FLATTEN(group), COUNT(crime_data);
counted_data = FOREACH counted_data GENERATE $0 AS (primary_type: chararray), $1 AS (year: int), $2 AS (count: long);

--Add one to year in order to join the count of current year with that of the previous year
pre_data = FOREACH counted_data GENERATE primary_type, year+1 AS (pre_year: int), count;

--Calculate the crime growth rate
compare_data = JOIN pre_data BY (primary_type, pre_year), counted_data BY (primary_type, year);
compare_data = FOREACH compare_data GENERATE $0 AS (primary_type: chararray), $1 AS (year:int), $2 AS (count: long), $5 AS (pre_count: long);
compare_data = FOREACH compare_data GENERATE primary_type, year, ROUND_TO(((double)count / (double)pre_count - 1) * 100, 2) AS (growth: double);

--Store the output to the file system
STORE compare_data INTO '/home/fanxia/Experiment/compare_data';