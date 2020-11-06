package br.com.silth.matcher;

import br.com.silth.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;

public class MatchersProprios {
    public static DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataAtualMatcher eHoje(Date date){
        return new DataAtualMatcher(date);
    }

    public static DataAtualMatcher eHojeComDiferencaDias(Integer dias){
        return new DataAtualMatcher(DataUtils.adicionarDias(new Date(), dias));
    }

    public static DataAtualMatcher eAmanha(Date date){
        return new DataAtualMatcher(DataUtils.adicionarDias(date, 1));
    }
}
