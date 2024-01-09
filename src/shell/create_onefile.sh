#!/bin/bash
# creation du fichier unique
verion_jar=1.0.2
jar_path=/home/marc/.m2/repository/org/yellowreindeer/onefile/${verion_jar}/onefile-${verion_jar}.jar
project_path=$(pwd)
main_path=${project_path}/src/main/java
if [ ! -d ${main_path} ]; then
  echo "Error the script must be excecuted at root project."
  exit 1
fi
mainClass=Player
out_dir=${project_path}/dist
if [ ! -d ${out_dir} ]; then
  mkdir ${out_dir}
  echo "creation out_dir: $out_dir"
fi
rm -f  ${mainClass}Onefile.java
excludeImport=org.yellowreindeer
# execute onefile
java -jar ${jar_path} ${mainClass} ${main_path} ${excludeImport} ${out_dir} | grep "traitement"