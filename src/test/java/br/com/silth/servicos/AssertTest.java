package br.com.silth.servicos;

import br.com.silth.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test(){
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1,1);
        Assert.assertEquals(0.3215, 0.32, 0.01);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        int i = 5;
        Integer i2 = 5;

        Assert.assertEquals("bola","bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = null;

        Assert.assertEquals(u1,u2);
        Assert.assertSame(u2,u2);
        Assert.assertNotSame(u2,u2);
        Assert.assertNull(u3);
        Assert.assertNotNull(u2);
    }
}
