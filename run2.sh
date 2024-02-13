#!/bin/sh

#SBATCH --account=combalgo
#SBATCH --partition=t4_normal_q
#SBATCH --nodes=1
#SBATCH --mem=128G
#SBATCH --time=72:00:00

javac *.java
java Runner