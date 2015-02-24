crime_data = LOAD '/user/chicago/input/testcrimes.csv' using PigStorage(',') AS (ID:chararray, CaseNum:chararray, Date:chararray, Block:chararray, Primary_Type:chararray, Description:chararray ,Arrest:boolean, Beat:chararray, District:chararray, Ward:chararray, Community_Area:chararray, Latitude:double, Longitude:double);

time_period = FOREACH crime_data GENERATE ID as ID, CONCAT( SUBSTRING(Date, 11, 13), SUBSTRING(Date, 20, 22)) as Time_Period;

grouped_time_period = GROUP time_period BY Time_Period;

time_period_counts = FOREACH grouped_time_period GENERATE group as Time_Period, COUNT(time_period) as counts;

ordered_period_counts = ORDER time_period_counts BY counts DESC;

DUMP ordered_period_counts;

