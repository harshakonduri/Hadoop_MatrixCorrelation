In this file i will list the steps to run my Hadoop project.

The Code for my Driver Mapper and Reducer are available in Hadoop_1
The jar used to run the Hadoop Scripts is in Hadoop_2 rush/ hadoop_run.jar is the jar file. The Script used to run is also present in the same directory.

I placed all the details for sample runs of 20 variables and 2 nodes and 50 variables and 64 nodes, and all the values for all configurations are detailed in the Report.

Hadoop_2/
-result_Observations/	
-netezza_verification/

Running a Hadoop Job:

Pick the hadoop_run.jar and the SLURM_script.sh

The hadoop_run.jar accepts 4 arguments ( inputFileName, outputFileName, number_of_Rows,number_of_Columns)

place these values in the SLURM_script and run the job.
The result will be placed in the outputFileName.

Verification:

copy the inputFileName and outputFileName to the netezza_verification directory.
please note: you should be logged into netezza to perform this verification.

To run verification for the Hadoop result follow these steps :

$ ./createAllProcedures <password>
$ ./createTable <number_of_rows> <number_of_columns> <password>
$ ./loadFile <Hadoop_inputFile> <Hadoop_outputFile> <password>
$ ./runVerification <number_of_rows> <number_of_columns> <password>

The tables persist across the database, so 

To Delete all the tables
$ ./deleteAllProcs <password>


The various files in the netezza_verification folder are below:

The netezza verification contains :
- loadFile : accepts 3 parameters, first parameter is inputFileName,second parameter is outputFileName, third parameter is netezza password
- createAllProcedures : accepts one parameter, netezza password
- runVerification : to run all the procedures
and 3 procedures :
- createProcedure : to create the necessary tables.
- resultProcedure : to calculate the verification from netezza using nza..corr_agg
- roundData : to compare the two tables obtained from hadoop output and netezza verification table.
- analytics_verification : To perform the Verification with Netezza Analytics library nza..corr_matrix_agg
- deleteprocs : To delete all the created tables and restore the system state.

