import math
import  collections
# class QGram:
#     def python_implementation(self, input_q, input_string):
#         regex = "".join('.' for i in range(input_q))
#         qgrams = sorted(set(re.findall(regex, input_string)))
#         print(qgrams)
#         indices_list = []
#         for qgram in qgrams:
#             string = input_string
#             indices = []
#             index = 0
#             index = string.find(qgram)
#             while index != -1:
#                 indices.append(index)
#                 string = string[:index] + '$' + string[index+1:]
#                 index = string.find(qgram)
#
#             indices_list.append(indices)
#         return indices_list
#
# f = open("string.txt", "r")
# string = f.read()
# qgram = QGram()
# print(len(string))
# indices = qgram.python_implementation(2, string)
# print(indices)







