REGISTER '/home/hduser/UDFQ4.jar';

crime_data = LOAD '/user/student022/input/crimes.csv' using PigStorage(',') AS (ID:chararray, CaseNum:chararray, Date:chararray, Block:chararray, Primary_Type:chararray, Description:chararray, Location_Description:chararray ,Arrest:boolean, Domestic:boolean, Beat:chararray, District:chararray, Ward:chararray, Community_Area:chararray, Latitude:double, Longitude:double);

location = FOREACH crime_data GENERATE ID as ID, UDFQ4.CloseToPlayground(Latitude, Longitude) as Closest_Park;

grouped_location  = GROUP location BY Closest_Park;

location_counts = FOREACH grouped_location GENERATE group as Closest_Park, COUNT(location) as counts;

ordered_counts = ORDER location_counts BY counts DESC;

DUMP ordered_counts;
