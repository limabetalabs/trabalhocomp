class A:
   
	def m(self):
		print ' 6 '
		print ' %d ' % 1
		print ' %d ' % int (1 + 1)
		d = 4 - 1
		print ' %d ' % d
		d = (6-3) + 1
		print ' %d ' % d
		d = 10 / 2
		print ' %d ' % d
		d = 2 * 3
		print ' %d ' % d
		e = 11 / 2
		print ' %.2f ' % e
		e = 11 / 2.
		print ' %.2f ' % e


print '';
print 'ok-Ger02'
print 'The output should be : '
print '6 1 2 3 4 5 6 5.00 5.50'
a = A()
a.m()

