class A:
   	i = 0
	def get(self):
		return self.i

	def put(self, p_i):
		self.i = p_i;

class B:
	num = 0
	a = A()
	def put(self, n):
		self.num = n
		a.put(n)
	def write(self):
		print self.num, a.get()
	def inc(self):
		self.num += self.num
	def getNum(self):
		return self.num

k = 0

a = A()
a.put(5)
k = a.get()
print k

b = B()
b.put(2)
b.inc()
b.write()
print b.getNum()

