from node import Node
from edit_distance import EditDistance
import math

class StandardPEXAlgorithm:
    def __init__(self):
        self.leaves = []
        self.indicesDict = {}

    def create_tree(self, subpattern, i, j, k, parent, plen):
        node = Node()
        node.start = i
        node.end = j
        left = math.ceil((k+1)/2.0)
        node.parent = parent
        node.error = k
        node.pattern = subpattern
        if k == 0:
            self.leaves.append(node)
        else:
            lk = math.floor((left * k) / (k + 1))
            self.create_tree(subpattern[i:i+left*plen], i, i+left*plen-1, lk, node, plen)
            rk = math.floor(((k + 1 - left) * k) / (k + 1))
            self.create_tree(subpattern[i + left*plen:j+1], i + left*plen, j, rk, node, plen)

    def find_indices(self, input_string):
        for leaf in self.leaves:
            string = input_string
            index = string.find(leaf.pattern)
            while index != -1:
                leaf.indices.append(index)
                string = string[:index] + '$' + string[index+1:]
                index = string.find(leaf.pattern)

    def search_candidates(self, input_string):
        editDistance = EditDistance()
        for leaf in self.leaves:
            for index in leaf.indices:
                i = leaf.start
                parent = leaf.parent
                cand = True
                p1 = -1
                while cand and parent != None:
                    p1 = index - (i - parent.start)
                    p2 = index + (parent.end - i) + 1
                    if p1 < 0:
                        p1 = 0
                    if p2 > len(input_string):
                        p2 = len(input_string)
                    distance = editDistance.compute(input_string[p1:p2], parent.pattern)
                    if distance <= parent.error:
                        parent = parent.parent
                    else:
                        p1 -= parent.error
                        counter = parent.error
                        withp1 = False
                        while counter != 0 and not withp1:
                            distance = editDistance.compute(input_string[p1:p2], parent.pattern)
                            if distance <= parent.error:
                                parent = parent.parent
                                withp1 = True
                            else:
                                counter -= 1
                                p1 += 1
                        if not withp1:
                            p2 += parent.error
                            counter = parent.error
                            withp2 = False
                            while counter != 0 and not withp2:
                                distance = editDistance.compute(input_string[p1:p2], parent.pattern)
                                if distance <= parent.error:
                                    parent = parent.parent
                                    withp2 = True
                                else:
                                    counter -= 1
                                    p2 -= 1
                            if not withp2:
                                cand = False
                if cand:
                    self.indicesDict[p1] = input_string[p1:p2]
                    