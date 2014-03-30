#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

typedef
  void (*Func)();

  /* class A { ... } */
typedef
struct _St_A {
  Func *vt;
  /* variável de instância i da classe A */
  int _A_i;
} _class_A;

_class_A *new_A(void);

int _A_get( _class_A *this ) {
  return this->_A_i;
}

void _A_put( _class_A *this, int _p_i ) {
  this->_A_i = _p_i;
}

  /* tabela de métodos da classe A -- virtual table */
Func VTclass_A[] = {
  ( void (*)() ) _A_get,
  ( void (*)() ) _A_put
  };

_class_A *new_A()
{
  _class_A *t;

  if ( (t = malloc(sizeof(_class_A))) != NULL )
    t->vt = VTclass_A;
  return t;
  }


typedef
  struct _St_B {
  Func *vt;
  _class_A *_B_a;
  int _B_num;
} _class_B;

_class_B *new_B(void);

void _B_put( _class_B *this, int _n )
{
  this->_B_num = _n;
  ( (void (*)(_class_A *, int)) this->_B_a->vt[1] )(this->_B_a, _n);

}

void _B_write ( _class_B *this )
{
  printf("%d %d\n", this->_B_num, ((int (*)(_class_A *)) this->_B_a->vt[0])( (_class_A *) this->_B_a));

  }

void _B_inc( _class_B *this ) 
{
  this->_B_num += this->_B_num; 
  }

int _B_getNum( _class_B *this )
{
  return this->_B_num;
}  

   /* apenas os métodos públicos  */
Func VTclass_B[] = {
  (void (*) () ) _B_put,
  (void (*) () ) _B_write,
  (void (*) () ) _B_inc,
  (void (*) () ) _B_getNum
  };

_class_B *new_B()
{
  _class_B *t;

  if ((t = malloc (sizeof(_class_B))) != NULL)
    t->vt = VTclass_B;
  t->_B_a = new_A();
  return t;
}

int main(){
     /* A a;  */
  _class_A *_a;
     /*  int k; */ 
  int _k;
  _class_B *_b;

     /* a = new A();  */
  _a = new_A();
     /* a.put(5);  */
  ( (void (*)(_class_A *, int)) _a->vt[1] )(_a, 5);
     /* k = a.get();  */
  _k = ( (int (*)(_class_A *)) _a->vt[0] )(_a);
     /* write(k);  */
  printf("%d\n", _k );

     /* b = new B();  */
  _b = new_B();
       /* b.put(2);  */
  ( (void (*)(_class_B *, int)) _b->vt[0] )(_b, 2);
       /* b.inc();  */
  ( (void (*)(_class_B *)) _b->vt[2] )(_b);
       /* b.print();  */
  ( (void (*)(_class_B *)) _b->vt[1] )(_b);
       /* write( b.getLastInc(), b.get() );  */
  printf("%d\n",   ( (int (*)(_class_B *)) _b->vt[3] )(_b) );

  return 0;
}

