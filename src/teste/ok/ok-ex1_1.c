/* deve-se incluir alguns headers porque algumas funções da biblioteca padrão de C são utilizadas na tradução. */

#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

/* define um tipo Func que é um ponteiro para função */
typedef
  void (*Func)();

/* Para cada classe, deve ser gerada uma estrutura como a abaixo. Se A tivesse variáveis de instância, estas seriam declaradas nesta estrutura. _class_A representa em C uma classe de Phython. */

typedef
  struct _St_A {
       /* ponteiro para um vetor de métodos da classe */
    Func *vt;
    } _class_A;

   /* Este é um protótipo de método que cria um objeto da classe Program. Toda classe 
      A possui um método new_A que cria e retorna um objeto da classe A.
      O método new_A é declarado antes do método main, abaixo.
   */
_class_A *new_A(void);

   /* Note que o método é traduzido para 
      uma função de C cujo nome é uma concatenação do nome da classe com o nome
      do método. Sempre há um primeiro parâmetro chamado this cujo tipo é a 
      estrutura que representa a classe */
void _A_m(_class_A *this){
	int _d;
	float _e;
	printf("6\n");
	printf("%d\n", 1);
	printf("%d\n", (int)(1+1));
	_d = 4 - 1;
	printf("%d\n", _d);
	_d = (6-3) + 1;
	printf("%d\n", _d);
	_d = 10 / 2;
	printf("%d\n", _d);
	_d = 2 * 3;
	printf("%d\n", _d);
	_e = 11 / 2;
	printf("%.2f\n", _e);
	_e = 11 / 2.0;
	printf("%.2f\n", _e);

}

  /* tabela de métodos da classe A -- virtual table */
Func VTclass_A[] = {
  ( void (*)() ) _A_m
  };


_class_A *new_A()
{
  _class_A *t;

  if ( (t = malloc(sizeof(_class_A))) != NULL )
    t->vt = VTclass_A;
  return t;
  }

  /* 
      Este é o método principal do programa.
   */
int main() {
	_class_A *_a;
	printf("\n");
	printf("ok-Ger02\n");
	printf("The output should be : \n");
	printf("6 1 2 3 4 5 6 5.00 5.50\n");
	_a = new_A();
	_a->vt[0](_a);

	return 0;
}


