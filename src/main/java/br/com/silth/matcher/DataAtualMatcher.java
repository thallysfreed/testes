package br.com.silth.matcher;

import br.com.silth.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DataAtualMatcher extends TypeSafeMatcher<Date> {
    private Date data;

    public DataAtualMatcher(Date data) {
        this.data = data;
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.isMesmaData(date, data);
    }

    @Override
    public void describeTo(Description description) {

    }
}
