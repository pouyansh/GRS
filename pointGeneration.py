import random
import numpy as np

d = 20
n = 40000

def generate(index):
    u = np.random.rand(d)
    u_hat = u / np.linalg.norm(u)
    v = np.random.rand(d)
    v_hat = v / np.linalg.norm(v)

    w = u_hat - np.dot(np.dot(u_hat, v_hat), v_hat)

    points = []

    k = np.random.binomial(n, 0.5, 1)[0]

    norm_1 = 0.25
    norm_2 = 0.75
    std = 0.1

    maxx = [0 for _ in range(d)]
    minx = [1 for _ in range(d)]

    for i in range(k):
        a = np.random.normal(norm_1, std)
        while a < 0 or a > 1:
            a = np.random.normal(norm_1, std)
        b = np.random.normal(norm_1, std)
        while b < 0 or b > 1:
            b = np.random.normal(norm_1, std)
        # a = np.random.rand()
        # b = np.random.rand()
        point = np.dot(a, v_hat) + np.dot(b, w)
        for i in range(d):
            if point[i] < minx[i]:
                minx[i] = point[i]
            if point[i] > maxx[i]:
                maxx[i] = point[i]
        points.append(np.dot(a, v_hat) + np.dot(b, w))

    for i in range(n - k):
        a = np.random.normal(norm_2, std)
        while a < 0 or a > 1:
            a = np.random.normal(norm_2, std)
        b = np.random.normal(norm_2, std)
        while b < 0 or b > 1:
            b = np.random.normal(norm_2, std)
        # a = np.random.rand()
        # b = np.random.rand()
        point = np.dot(a, v_hat) + np.dot(b, w)
        for i in range(d):
            if point[i] < minx[i]:
                minx[i] = point[i]
            if point[i] > maxx[i]:
                maxx[i] = point[i]
        points.append(np.dot(a, v_hat) + np.dot(b, w))

    for point in points:
        for i in range(d):
            point[i] = (point[i] - minx[i]) / (maxx[i] - minx[i]) + minx[i]
    
    with open("./Datasets/uniform-100d/pointset_" + str(index) + ".txt", 'w') as f:
        for point in points:
            for i in range(d):
                f.write(str(point[i]) + " ")
            f.write("\n")

for i in range(10):
    generate(i)