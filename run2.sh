#!/bin/sh

#SBATCH --account=combalgo
#SBATCH --partition=normal_q
#SBATCH --nodes=1
#SBATCH --mem=128G
#SBATCH --time=144:00:00

javac *.java
java Runner