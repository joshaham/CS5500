import os
F='TeamTestProgram'
T=F+'.java'
os.system('cp ' + T +' ./joshua/')
os.system('cp ' + T +' ./moses/')
os.system('cp ' + T +' ./zhuoli/')
os.system('make')
os.system('make run')


