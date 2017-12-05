import time
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle, Circle

# while(True):
for i in range(0,100000):

    with open('../thermo-vals.txt') as f:
        content = f.readlines()
        content = [x.strip('\n') for x in content]
    for line in content:
        the_key = line.split(':')[0]
        the_value = line.split(':')[1]
        if the_key == 'BoilerTemp':
            BoilerTemp = float(the_value)
        if the_key == 'PortionBrewed':
            PortionBrewed = float(the_value)

    with open('../control-vals.txt') as f:
        content = f.readlines()
        content = [x.strip('\n') for x in content]
    for line in content:
        the_key = line.split(':')[0]
        the_value = line.split(':')[1]
        if the_key == 'OutUser':
            OutUser = the_value

    with open('../pot-vals.txt') as f:
        content = f.readlines()
        content = [x.strip('\n') for x in content]
    for line in content:
        the_key = line.split(':')[0]
        the_value = line.split(':')[1]
        if the_key == 'PotTemp':
            PotTemp = float(the_value)
            
    plt.figure(1)
            
    plt.axis([0,100,0,100])
    plt.ion()
    plt.show()

    currentAxis = plt.gca()
    currentAxis.add_patch(Rectangle((10, 20), 30, 70, facecolor="black"))
    currentAxis.add_patch(Rectangle((30, 10), 60, 20, facecolor="black"))
    currentAxis.add_patch(Rectangle((10, 10), 20, 10, facecolor="black"))
    currentAxis.add_patch(Rectangle((30, 80), 60, 10, facecolor="black"))
    currentAxis.add_patch(Rectangle((60, 75), 10, 5, facecolor="black"))
    currentAxis.add_patch(Rectangle((15, 25), 20, 60, facecolor="grey"))
    currentAxis.add_patch(Rectangle((15, 25), 20, 60*(1-PortionBrewed), facecolor="blue"))
    currentAxis.add_patch(Rectangle((50, 30), 30, 40, facecolor="grey"))
    currentAxis.add_patch(Rectangle((50, 30), 30, 40*(PortionBrewed), facecolor=[77/255, 38/255, 0]))

    if PortionBrewed < 100:
        if len(str(BoilerTemp)) > 5:
            temp_txt = plt.text(20, 40, str(BoilerTemp)[0:5] + ' C', color="white")
        else:
            temp_txt = plt.text(20, 40, str(BoilerTemp) + ' C', color="white")
    if PortionBrewed > 0:
        if len(str(PotTemp)) > 5:
            pot_txt = plt.text(60, 40, str(PotTemp)[0:5] + ' C', color="white")
        else:
            pot_txt = plt.text(60, 40, str(PotTemp) + ' C', color="white")

    if (OutUser == 'Brewing'):
        currentAxis.add_patch(Circle((55,22), 3, facecolor="green"))
        currentAxis.add_patch(Circle((75,22), 3, facecolor="grey"))
    elif (OutUser == 'Ready'):
        currentAxis.add_patch(Circle((55,22), 3, facecolor="grey"))
        currentAxis.add_patch(Circle((75,22), 3, facecolor="green"))
    else:
        currentAxis.add_patch(Circle((55,22), 3, facecolor="grey"))
        currentAxis.add_patch(Circle((75,22), 3, facecolor="grey"))

    plt.text(50, 15, 'Brewing', color='white')
    plt.text(71, 15, 'Ready', color='white')
    
    plt.plot([0,100],[0,0])
    plt.draw()
    plt.pause(0.1)

    if PortionBrewed < 100:
        temp_txt.remove()
    if PortionBrewed > 0:
        pot_txt.remove()


