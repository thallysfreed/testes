package br.com.silth.servicos;

import br.com.silth.exception.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CalculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup(){
        calculadora = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        int a = 5, b = 7;
        int resultadoEsperado = 12;

        int resultado = calculadora.somar(a,b);

        Assert.assertThat(resultado, is(resultadoEsperado));
    }

    @Test
    public void deveSubtrairDoisValores(){
        int a = 10, b =5;
        int resultadoEsperado = 5;

        int resultado = calculadora.subtrair(a,b);


        Assert.assertThat(resultado, is(resultadoEsperado));

    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        int a = 10, b = 5;
        int resultadoEsperado = 2;

        int resultado = calculadora.dividir(a,b);

        Assert.assertThat(resultado, is(equalTo(resultadoEsperado)));
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExceptionCasoDividaPorZero() throws NaoPodeDividirPorZeroException {
        int a = 10, b = 0;

        calculadora.dividir(a, b);
    }
}
