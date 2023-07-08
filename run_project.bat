@echo off

REM Clone "back/back" branch into the "back" folder
git clone --branch back/back https://github.com/bilelfeki/PFE.git back

REM Change to the "back" directory
cd back

REM Run "mvn clean install"
call mvn clean install

REM Change to the "target" directory
cd target

REM Run the JAR file
start java -jar fileIntegrator-0.0.1-SNAPSHOT.jar

REM Return to the original directory
cd ../../

REM Clone "front/front" branch into the "front" folder
git clone --branch front/front https://github.com/bilelfeki/PFE.git front

REM Change to the "front" directory
cd front

REM Install npm dependencies
call npm install

REM Start the Angular development server
start ng s -o


REM Return to the original directory
cd ../

REM Clone "executable/executable" branch into the "executable" folder
git clone --branch executable/executable https://github.com/bilelfeki/PFE.git executable

set "sourceFolder=%cd%\executable"
set "destinationFolder=%userprofile%\Desktop\executable"

xcopy "%sourceFolder%" "%destinationFolder%" /E /I /Y
echo Cloning, building, and running completed.