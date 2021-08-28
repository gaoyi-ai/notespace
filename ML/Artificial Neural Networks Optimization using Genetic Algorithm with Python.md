---
title: Artificial Neural Networks Optimization using Genetic Algorithm with Python
categories:
- ML
- GA
tags:
- GA
date: 2021/8/28
---



# Artificial Neural Networks Optimization using Genetic Algorithm with Python



In a previous tutorial titled “**Artificial Neural Network Implementation using NumPy and Classification of the Fruits360 Image Dataset**” available in my LinkedIn profile at this [link](https://www.linkedin.com/pulse/artificial-neural-network-implementation-using-numpy-fruits360-gad), an artificial neural network (ANN) is created for classifying 4 classes of the Fruits360 image dataset. The source code used in this tutorial is available in my [GitHub page](https://github.com/ahmedfgad/NumPyANN).

This tutorial is also available at TowardsDataScience [here](https://towardsdatascience.com/artificial-neural-network-implementation-using-numpy-and-classification-of-the-fruits360-image-3c56affa4491).

A quick summary of this tutorial is extracting the feature vector (360 bins hue channel histogram) and reducing it to just 102 element by using a filter-based technique using the standard deviation. Later, the ANN is built from scratch using NumPy.

The ANN was not completely created as just the forward pass was made ready but there is no backward pass for updating the network weights. This is why the accuracy is very low and not exceeds 45%. The solution to this problem is using an optimization technique for updating the network weights. This tutorial uses the genetic algorithm (GA) for optimizing the network weights.

It is worth-mentioning that both the previous and this tutorial are based on my 2018 book cited as “**Ahmed Fawzy Gad ‘Practical Computer Vision Applications Using Deep Learning with CNNs’. Dec. 2018, Apress, 978–1–4842–4167–7** “. The book is available at Springer at [this link](https://springer.com/us/book/9781484241660). You can find all details within this book.

The source code used in this tutorial is available in my GitHub page [here](https://github.com/ahmedfgad/NeuralGenetic).

# Read More about Genetic Algorithm

Before starting this tutorial, I recommended reading about how the genetic algorithm works and its implementation in Python using NumPy from scratch based on my previous tutorials found at the links listed in the Resources section at the end of the tutorial.

After understanding how GA works based on numerical examples in addition to implementation using Python, we can start using GA to optimize the ANN by updating its weights (parameters).

# Using GA with ANN

GA creates multiple solutions to a given problem and evolves them through a number of generations. Each solution holds all parameters that might help to enhance the results. For ANN, weights in all layers help achieve high accuracy. Thus, a single solution in GA will contain all weights in the ANN. According to the network structure discussed in the previous tutorial and given in the figure below, the ANN has 4 layers (1 input, 2 hidden, and 1 output). Any weight in any layer will be part of the same solution. A single solution to such network will contain a total number of weights equal to 102x150+150x60+60x4=24,540. If the population has 8 solutions with 24,540 parameters per solution, then the total number of parameters in the entire population is 24,540x8=196,320.

![img](https://miro.medium.com/max/1400/1*OzJCA69AW19b1IgRjlKIXQ.png)

Looking at the above figure, the parameters of the network are in matrix form because this makes calculations of ANN much easier. For each layer, there is an associated weights matrix. Just multiply the inputs matrix by the parameters matrix of a given layer to return the outputs in such layer. Chromosomes in GA are 1D vectors and thus we have to convert the weights matrices into 1D vectors.

Because matrix multiplication is a good option to work with ANN, we will still represent the ANN parameters in the matrix form when using the ANN. Thus, matrix form is used when working with ANN and vector form is used when working with GA. This makes us need to convert the matrix to vector and vice versa. The next figure summarizes the steps of using GA with ANN. This figure is referred to as the **main figure**.

![img](https://miro.medium.com/max/833/1*EJptAF_dhtZrsGNem6O3bQ.png)

# Weights Matrices to 1D Vector

Each solution in the population will have two representations. First is a 1D vector for working with GA and second is a matrix to work with ANN. Because there are 3 weights matrices for the 3 layers (2 hidden + 1 output), there will be 3 vectors, one for each matrix. Because a solution in GA is represented as a single 1D vector, such 3 individual 1D vectors will be concatenated into a single 1D vector. Each solution will be represented as a vector of length 24,540. The next Python code creates a function named **mat_to_vector()** that converts the parameters of all solutions within the population from matrix to vector.

```
def mat_to_vector(mat_pop_weights):
     pop_weights_vector = []
     for sol_idx in range(mat_pop_weights.shape[0]):
         curr_vector = []
         for layer_idx in range(mat_pop_weights.shape[1]):
             vector_weights = numpy.reshape(mat_pop_weights[sol_idx, layer_idx], newshape=(mat_pop_weights[sol_idx, layer_idx].size))
             curr_vector.extend(vector_weights)
         pop_weights_vector.append(curr_vector)
     return numpy.array(pop_weights_vector)
```

The function accepts an argument representing the population of all solutions in order to loop through them and return their vector representation. At the beginning of the function, an empty list variable named **pop_weights_vector** is created to hold the result (vectors of all solutions). For each solution in matrix form, there is an inner loop that loops through its three matrices. For each matrix, it is converted into a vector using the **numpy.reshape()** function which accepts the input matrix and the output size to which the matrix will be reshaped. The variable **curr_vector** accepts all vectors for a single solution. After all vectors are generated, they get appended into the **pop_weights_vector** variable.

Note that we used the **numpy.extend()** function for vectors belonging to the same solution and **numpy.append()** for vectors belonging to different solutions. The reason is that **numpy.extend()** takes the numbers within the 3 vectors belonging to the same solution and concatenate them together. In other words, calling this function for two lists returns a new single list with numbers from both lists. This is suitable in order to create just a 1D chromosome for each solution. But **numpy.append()** will return three lists for each solution. Calling it for two lists, it returns a new list which is split into two sub-lists. This is not our objective. Finally, the function **mat_to_vector()** returns the population solutions as a NumPy array for easy manipulation later.

# Implementing GA Steps

After converting all solutions from matrices to vectors and concatenated together, we are ready to go through the GA steps discussed in the tutorial titled “**Introduction to Optimization with Genetic Algorithm**”. The steps are presented in the **main figure** and also summarized in the next figure.

![img](https://miro.medium.com/max/1400/1*KenWhL8FnRHflkVYenhtHA.png)

Remember that GA uses a fitness function to returns a fitness value for each solution. The higher the fitness value the better the solution. The best solutions are returned as parents in the **parents selection** step.

One of the common fitness functions for a classifier such as ANN is the accuracy. It is the ratio between the correctly classified samples and the total number of samples. It is calculated according to the next equation. The classification accuracy of each solution is calculated according to steps in the **main figure**.

![img](https://miro.medium.com/max/330/1*2uB29jyAbdzDhrY4GdTsew.jpeg)

The single 1D vector of each solution is converted back into 3 matrices, one matrix for each layer (2 hidden and 1 output). Conversion takes place using a function called **vector_to_mat()**. It is defined in the next code.

```
def vector_to_mat(vector_pop_weights, mat_pop_weights):
    mat_weights = []
    for sol_idx in range(mat_pop_weights.shape[0]):
        start = 0
        end = 0
        for layer_idx in range(mat_pop_weights.shape[1]):
            end = end + mat_pop_weights[sol_idx, layer_idx].size
            curr_vector = vector_pop_weights[sol_idx, start:end]
            mat_layer_weights = numpy.reshape(curr_vector, newshape=(mat_pop_weights[sol_idx, layer_idx].shape))
            mat_weights.append(mat_layer_weights)
            start = end
    return numpy.reshape(mat_weights, newshape=mat_pop_weights.shape)
```

It reverses the work done previously. But there is an important question. If the vector of a given solution is just one piece, how we can split into three different parts, each part represents a matrix? The size of the first parameters matrix between the input layer and the hidden layer is 102x150. When being converted into a vector, its length will be 15,300. Because it is the first vector to be inserted in the **curr_vector** variable according to the **mat_to_vector()** function, then its indices start from index 0 and end at index 15,299. The **mat_pop_weights** is used as an argument for the **vector_to_mat()** function in order to know the size of each matrix. We are not interested in using the weights from the **mat_pop_weights** variable but just the matrices sizes are used from it.

For the second vector in the same solution, it will be the result of converting a matrix of size 150x60. Thus the vector length is 9,000. Such a vector is inserted into the **curr_vector** variable just before the previous vector of length 15,300. As a result, it will start from index 15,300 and ends at index 15,300+9,000–1=24,299. The -1 is used because Python starts indexing at 0. For the last vector created from the parameters matrix of size 60x4, its length is 240. Because it is added into the **curr_vector** variable exactly after the previous vector of length 9,000, then its index will start after it. That is its start index is 24,300 and its end index is 24,300+240–1=24,539. So, we can successfully restore the vector into the original 3 matrices.

The matrices returned for each solution are used to predict the class label for each of the 1,962 samples in the used dataset to calculate the accuracy. This is done using 2 functions which are **predict_outputs()** and **fitness()** according to the next code.

```
def predict_outputs(weights_mat, data_inputs, data_outputs, activation="relu"):
    predictions = numpy.zeros(shape=(data_inputs.shape[0]))
    for sample_idx in range(data_inputs.shape[0]):
        r1 = data_inputs[sample_idx, :]
        for curr_weights in weights_mat:
            r1 = numpy.matmul(a=r1, b=curr_weights)
            if activation == "relu":
                r1 = relu(r1)
            elif activation == "sigmoid":
                r1 = sigmoid(r1)
        predicted_label = numpy.where(r1 == numpy.max(r1))[0][0]
        predictions[sample_idx] = predicted_label
    correct_predictions = numpy.where(predictions == data_outputs)[0].size
    accuracy = (correct_predictions/data_outputs.size)*100
    return accuracy, predictions

def fitness(weights_mat, data_inputs, data_outputs, activation="relu"):
    accuracy = numpy.empty(shape=(weights_mat.shape[0]))
    for sol_idx in range(weights_mat.shape[0]):
        curr_sol_mat = weights_mat[sol_idx, :]
        accuracy[sol_idx], _ = predict_outputs(curr_sol_mat, data_inputs, data_outputs, activation=activation)
    return accuracy
```

The **predict_outputs()** function accepts the weights of a single solution, inputs, and outputs of the training data, and an optional parameter that specifies which activation function to use. It returns the accuracy of just one solution not all solutions within the population. It order to return the fitness value (i.e. accuracy) of all solutions within the population, the **fitness()** function loops through each solution, pass it to the **predict_outputs()** function, store the accuracy of all solutions into the **accuracy** array, and finally return such an array.

After calculating the fitness value (i.e. accuracy) for all solutions, the remaining steps of GA in the main figure are applied the same way done previously. The best parents are selected, based on their accuracy, into the mating pool. Then mutation and crossover variants are applied in order to produce the offspring. The population of the new generation is created using both offspring and parents. These steps are repeated for a number of generations.

# Complete Python Implementation

The Python implementation for such project has three Python files:

1. **ga.py** for implementing GA functions.
2. **ANN.py** for implementing ANN functions.
3. Third file for calling such functions through a number of generations. This is the main file of the project.

# Main Project File Implementation

The third file is the main file because it connects all functions. It reads the features and the class labels files, filters features based on the standard deviation, creates the ANN architecture, generates the initial solutions, loops through a number of generations by calculating the fitness values for all solutions, selecting best parents, applying crossover and mutation, and finally creating the new population. Its implementation is given below. Such a file defines the GA parameters such as a number of solutions per population, number of selected parents, mutation percent, and number of generations. You can try different values for them.

```
import numpy
import GA
import pickle
import ANN
import matplotlib.pyplot

f = open("dataset_features.pkl", "rb")
data_inputs2 = pickle.load(f)
f.close()
features_STDs = numpy.std(a=data_inputs2, axis=0)
data_inputs = data_inputs2[:, features_STDs>50]


f = open("outputs.pkl", "rb")
data_outputs = pickle.load(f)
f.close()

#Genetic algorithm parameters:
#    Mating Pool Size (Number of Parents)
#    Population Size
#    Number of Generations
#    Mutation Percent

sol_per_pop = 8
num_parents_mating = 4
num_generations = 1000
mutation_percent = 10

#Creating the initial population.
initial_pop_weights = []
for curr_sol in numpy.arange(0, sol_per_pop):
    HL1_neurons = 150
    input_HL1_weights = numpy.random.uniform(low=-0.1, high=0.1, 

                                             size=(data_inputs.shape[1], HL1_neurons))

    HL2_neurons = 60
    HL1_HL2_weights = numpy.random.uniform(low=-0.1, high=0.1, 

                                             size=(HL1_neurons, HL2_neurons))

    output_neurons = 4
    HL2_output_weights = numpy.random.uniform(low=-0.1, high=0.1, 

                                              size=(HL2_neurons, output_neurons))

    initial_pop_weights.append(numpy.array([input_HL1_weights, 

                                                HL1_HL2_weights, 

                                                HL2_output_weights]))

pop_weights_mat = numpy.array(initial_pop_weights)
pop_weights_vector = ga.mat_to_vector(pop_weights_mat)

best_outputs = []
accuracies = numpy.empty(shape=(num_generations))

for generation in range(num_generations):
    print("Generation : ", generation)

    # converting the solutions from being vectors to matrices.
    pop_weights_mat = ga.vector_to_mat(pop_weights_vector, 
                                       pop_weights_mat)

    # Measuring the fitness of each chromosome in the population.
    fitness = ANN.fitness(pop_weights_mat, 
                          data_inputs, 
                          data_outputs, 
                          activation="sigmoid")

    accuracies[generation] = fitness[0]
    print("Fitness")
    print(fitness)

    # Selecting the best parents in the population for mating.
    parents = ga.select_mating_pool(pop_weights_vector, 

                                    fitness.copy(), 

                                    num_parents_mating)
    print("Parents")
    print(parents)

    # Generating next generation using crossover.
    offspring_crossover = ga.crossover(parents,

                                       offspring_size=(pop_weights_vector.shape[0]-parents.shape[0], pop_weights_vector.shape[1]))

    print("Crossover")
    print(offspring_crossover)

    # Adding some variations to the offsrping using mutation.
    offspring_mutation = ga.mutation(offspring_crossover, 

                                     mutation_percent=mutation_percent)
    print("Mutation")
    print(offspring_mutation)

    # Creating the new population based on the parents and offspring.
    pop_weights_vector[0:parents.shape[0], :] = parents
    pop_weights_vector[parents.shape[0]:, :] = offspring_mutation

pop_weights_mat = ga.vector_to_mat(pop_weights_vector, pop_weights_mat)
best_weights = pop_weights_mat [0, :]
acc, predictions = ANN.predict_outputs(best_weights, data_inputs, data_outputs, activation="sigmoid")
print("Accuracy of the best solution is : ", acc)

matplotlib.pyplot.plot(accuracies, linewidth=5, color="black")
matplotlib.pyplot.xlabel("Iteration", fontsize=20)
matplotlib.pyplot.ylabel("Fitness", fontsize=20)
matplotlib.pyplot.xticks(numpy.arange(0, num_generations+1, 100), fontsize=15)
matplotlib.pyplot.yticks(numpy.arange(0, 101, 5), fontsize=15)

f = open("weights_"+str(num_generations)+"_iterations_"+str(mutation_percent)+"%_mutation.pkl", "wb")
pickle.dump(pop_weights_mat, f)
f.close()
```

Based on 1,000 generations, a plot is created at the end of this file using **Matplotlib** visualization library that shows how the accuracy changes across each generation. It is shown in the next figure.

![img](https://miro.medium.com/max/833/1*mdIbL6CfZ-7tuLZhDRaZ3A.png)

After 1,000 iterations, the accuracy is more than 97%. This is compared to 45% without using an optimization technique as in the previous tutorial. This is an evidence about why results might be bad not because there is something wrong in the model or the data but because no optimization technique is used. Of course, using different values for the parameters such as 10,000 generations might increase the accuracy. At the end of this file, it saves the parameters in matrix form to the disk for use later.

# ga.py Implementation

The **ga.py** file implementation is in listed below. Note that the **mutation()** function accepts the **mutation_percent** parameter that defines the number of genes to change their values randomly. It is set to 10% in the main file. Such a file holds the 2 new functions **mat_to_vector()** and **vector_to_mat()**.

```
import numpy
import random

# Converting each solution from matrix to vector.
def mat_to_vector(mat_pop_weights):
    pop_weights_vector = []
    for sol_idx in range(mat_pop_weights.shape[0]):
        curr_vector = []
        for layer_idx in range(mat_pop_weights.shape[1]):
            vector_weights = numpy.reshape(mat_pop_weights[sol_idx, layer_idx], newshape=(mat_pop_weights[sol_idx, layer_idx].size))
            curr_vector.extend(vector_weights)
        pop_weights_vector.append(curr_vector)
    return numpy.array(pop_weights_vector)

# Converting each solution from vector to matrix.
def vector_to_mat(vector_pop_weights, mat_pop_weights):
    mat_weights = []
    for sol_idx in range(mat_pop_weights.shape[0]):
        start = 0
        end = 0
        for layer_idx in range(mat_pop_weights.shape[1]):
            end = end + mat_pop_weights[sol_idx, layer_idx].size
            curr_vector = vector_pop_weights[sol_idx, start:end]
            mat_layer_weights = numpy.reshape(curr_vector, newshape=(mat_pop_weights[sol_idx, layer_idx].shape))
            mat_weights.append(mat_layer_weights)
            start = end
    return numpy.reshape(mat_weights, newshape=mat_pop_weights.shape)

def select_mating_pool(pop, fitness, num_parents):
    # Selecting the best individuals in the current generation as parents for producing the offspring of the next generation.
    parents = numpy.empty((num_parents, pop.shape[1]))
    for parent_num in range(num_parents):
        max_fitness_idx = numpy.where(fitness == numpy.max(fitness))
        max_fitness_idx = max_fitness_idx[0][0]
        parents[parent_num, :] = pop[max_fitness_idx, :]
        fitness[max_fitness_idx] = -99999999999
    return parents

def crossover(parents, offspring_size):
    offspring = numpy.empty(offspring_size)
    # The point at which crossover takes place between two parents. Usually, it is at the center.
    crossover_point = numpy.uint32(offspring_size[1]/2)
    for k in range(offspring_size[0]):
        # Index of the first parent to mate.
        parent1_idx = k%parents.shape[0]
        # Index of the second parent to mate.
        parent2_idx = (k+1)%parents.shape[0]
        # The new offspring will have its first half of its genes taken from the first parent.
        offspring[k, 0:crossover_point] = parents[parent1_idx, 0:crossover_point]
        # The new offspring will have its second half of its genes taken from the second parent.
        offspring[k, crossover_point:] = parents[parent2_idx, crossover_point:]
    return offspring


def mutation(offspring_crossover, mutation_percent):
    num_mutations = numpy.uint32((mutation_percent*offspring_crossover.shape[1])/100)
    mutation_indices = numpy.array(random.sample(range(0, offspring_crossover.shape[1]), num_mutations))
    # Mutation changes a single gene in each offspring randomly.
    for idx in range(offspring_crossover.shape[0]):
        # The random value to be added to the gene.
        random_value = numpy.random.uniform(-1.0, 1.0, 1)
        offspring_crossover[idx, mutation_indices] = offspring_crossover[idx, mutation_indices] + random_value
    return offspring_crossover
```

# ANN.py Implementation

Finally, the **ANN.py** is implemented according to the code listed below. It contains the implementation of the activation functions (sigmoid and ReLU) in addition to the **fitness()** and **predict_outputs()** functions to calculate the accuracy.

```
import numpy

def sigmoid(inpt):
    return 1.0 / (1.0 + numpy.exp(-1 * inpt))

def relu(inpt):
    result = inpt
    result[inpt < 0] = 0
    return result

def predict_outputs(weights_mat, data_inputs, data_outputs, activation="relu"):
    predictions = numpy.zeros(shape=(data_inputs.shape[0]))
    for sample_idx in range(data_inputs.shape[0]):
        r1 = data_inputs[sample_idx, :]
        for curr_weights in weights_mat:
            r1 = numpy.matmul(a=r1, b=curr_weights)
            if activation == "relu":
                r1 = relu(r1)
            elif activation == "sigmoid":
                r1 = sigmoid(r1)
        predicted_label = numpy.where(r1 == numpy.max(r1))[0][0]
        predictions[sample_idx] = predicted_label
    correct_predictions = numpy.where(predictions == data_outputs)[0].size
    accuracy = (correct_predictions / data_outputs.size) * 100
    return accuracy, predictions

def fitness(weights_mat, data_inputs, data_outputs, activation="relu"):
    accuracy = numpy.empty(shape=(weights_mat.shape[0]))
    for sol_idx in range(weights_mat.shape[0]):
        curr_sol_mat = weights_mat[sol_idx, :]
        accuracy[sol_idx], _ = predict_outputs(curr_sol_mat, data_inputs, data_outputs, activation=activation)
    return accuracy
```

# Resources

- Introduction to Optimization with Genetic Algorithm

https://www.linkedin.com/pulse/introduction-optimization-genetic-algorithm-ahmed-gad/

https://www.kdnuggets.com/2018/03/introduction-optimization-with-genetic-algorithm.html

https://towardsdatascience.com/introduction-to-optimization-with-genetic-algorithm-2f5001d9964b

https://www.springer.com/us/book/9781484241660

- Genetic Algorithm (GA) Optimization — Step-by-Step Example

https://www.slideshare.net/AhmedGadFCIT/genetic-algorithm-ga-optimization-stepbystep-example

- Genetic Algorithm Implementation in Python

https://www.linkedin.com/pulse/genetic-algorithm-implementation-python-ahmed-gad/

https://www.kdnuggets.com/2018/07/genetic-algorithm-implementation-python.html

[Genetic Algorithm Implementation in PythonThis tutorial will implement the genetic algorithm optimization technique in Python based on a simple example in which…towardsdatascience.com](https://towardsdatascience.com/genetic-algorithm-implementation-in-python-5ab67bb124a6)

https://github.com/ahmedfgad/GeneticAlgorithmPython