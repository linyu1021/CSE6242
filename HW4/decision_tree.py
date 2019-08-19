from util import entropy, information_gain, partition_classes
import numpy as np 
import ast
import random
import math

# References: https://github.com/brainliu/python/blob/master/trees.py
def majorityCnt(label):
    if len(label) / 2 > sum(label):
        return 0
    else:
        return 1


class DecisionTree(object):
    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        #self.tree = []
        self.tree = {}
        

    def learn(self, X, y):
        # TODO: Train the decision tree (self.tree) using the the dataset X and labels y
        # You will have to make use of the functions in utils.py to train the tree
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)
        #self.tree = self.__build_tree__(X, y)
        index = range(len(X[1]))
        random.shuffle(index) # randomly ordered the indices
        self.tree = one_tree_split(self, X, y, index[0: int(math.sqrt(len(X[1])))]) #sqrt
        #return self.tree
        
    def classify(self, record):
        # TODO: classify the record using self.tree and return the predicted label
        mytree = self.tree
        while isinstance(mytree, dict):# Recursively go down through a decision tree
            attribute = mytree.keys()[0]
            value_record = float(record[attribute])
            mytree = mytree[attribute]
            value_str = mytree.keys()[0]
            if value_str[:5] == 'right':
                value_split = float(value_str[5:])
            else:
                value_split = float(value_str[4:])
            if value_record <= value_split:   # If the value of that attribute is smaller than the split value, it goes down the left branch
                mytree = mytree[mytree.keys()[1]]
            else:
                mytree = mytree[mytree.keys()[0]]

        return int(mytree)


def best_feature_split(self, X, y, label):  
        info_gain, attr, thresh, index  = 0, 0, 0, 0
        count_label = -1

        for i in label:
            count_label += 1
            if(isinstance(label, int)):
                value = X[:,i] 
                #np.sort(vals)
                sum = 0.0

                for row in X:
                    sum += float(row[i])
                thresh = sum / float(len(X))
                splity = partition_classes(value, y, thresh)
                attr = i
                index = count_label
                new_info_gain = information_gain(y, splity)

                # if new_info_gain > info_gain:
                #     info_gain = new_info_gain
                #     attr = i 
                #     thresh = new_thresh
                #     index = count_label
            else:
                value = X[:, i]
                np.unique(value)
                for row in X:
                        new_thresh = value[i]
                        splity = partition_classes(value, y, new_thresh)
                        new_info_gain = information_gain(y, splity)

                        if new_info_gain > info_gain:
                            info_gain = new_info_gain
                            attr = i 
                            thresh = new_thresh
                            index = count_label
                #X_left, y_left, X_right, y_right = partition_classes(X, y, attr, thresh)
        return attr, thresh, index 
        #X_left, y_left, X_right, y_right


def one_tree_split(self, X, y, depth):
    numEntries = len(X)
    if(numEntries <= 1):
        return majorityCnt(y)
    if depth > 5:
        return majorityCnt(y)
    if y.count(1) == len(y) or y.count(0) == len(y):
        return majorityCnt(y)
    else: 
        attr, thresh, index = best_feature_split(X, y, label)
        mytree = {index_best:{}} # dictionary
        del (X[attr])
        mytree[attr]["left" + str(thresh)] = one_tree_split(self, X_left, y_left, depth + 1)
        mytree[attr]["right" + str(thresh)] = one_tree_split(self, X_right, y_right, depth + 1)
        return mytree




