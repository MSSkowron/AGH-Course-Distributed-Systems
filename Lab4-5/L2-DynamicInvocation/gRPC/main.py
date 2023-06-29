import os

requestAdd = '''{"a": %s, "b": %s}''' % (1, 2)
os.system("echo " + requestAdd + " | evans -r cli call calculator.Calculator.Add")

requestSubtract = '''{"a": %s, "b": %s}''' % (5, 3)
os.system("echo " + requestSubtract + " | evans -r cli call calculator.Calculator.Subtract")

requestOp = '''{"a1": {"a": %s, "b": %s, "c": %s, "d": "%s"}, "b1": %s}''' % (10, 5, 2.5, "test", 7)
os.system("echo " + requestOp + " | evans -r cli call calculator.Calculator.Op")

requestAvg = '''{"n": {"numbers": [%s, %s, %s, %s, %s, %s, %s, %s, %s, %s]}}''' % (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
os.system("echo " + requestAvg + " | evans -r cli call calculator.Calculator.Avg")
