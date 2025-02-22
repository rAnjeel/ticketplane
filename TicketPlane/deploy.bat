@echo off
setlocal enabledelayedexpansion

:: Déclaration des variables
set "work_dir=D:\Github\ticketplane\TicketPlane"
set "temp=%work_dir%\temp"
set "web_xml=%work_dir%\config\web.xml"
set "lib=%work_dir%\lib"
set "web_apps=C:\wildfly-33.0.1.Final\wildfly-33.0.1.Final\standalone\deployments"
set "war_name=TicketPlane"
set "Binary=%work_dir%\bin"
set "views_source=%work_dir%\page"

:: Effacer le dossier [temp]
if exist "%temp%" (
    rd /s /q "%temp%"
)

:: Créer la structure WildFly standard
mkdir "%temp%"
mkdir "%temp%\WEB-INF"
mkdir "%temp%\WEB-INF\lib"
mkdir "%temp%\WEB-INF\classes"
mkdir "%temp%\WEB-INF\views"
mkdir "%temp%\resources"
mkdir "%temp%\resources\assets"

:: Copier les fichiers compilés (.class)
xcopy /E /y "%Binary%\*" "%temp%\WEB-INF\classes\"

:: Copier le web.xml
copy "%web_xml%" "%temp%\WEB-INF\"

:: Copier les librairies (.jar)
xcopy /s /i "%lib%\*.jar" "%temp%\WEB-INF\lib\"

:: Copier les JSP à la racine
xcopy /E /y "%views_source%\*.jsp" "%temp%\"

:: Copier les assets dans resources
xcopy /E /I /y "%views_source%\assets" "%temp%\resources\assets\"

:: Créer le WAR
cd "%temp%"
jar cf "%work_dir%\%war_name%.war" *

:: Déployer dans WildFly
if exist "%web_apps%\%war_name%.war" (
    del /f /q "%web_apps%\%war_name%.war"
)
copy /y "%work_dir%\%war_name%.war" "%web_apps%"
del /f /q "%work_dir%\%war_name%.war"

echo Déploiement terminé.
