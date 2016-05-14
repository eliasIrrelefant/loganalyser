package it.eup.loganalyser.importfilter;

import java.util.function.Predicate;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import it.eup.loganalyser.entity.LogDataRow;

public class JavascriptInputFilterFactory {

    @SuppressWarnings("unchecked")
    public static Filterable createNew(String script) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("nashorn");
        Invocable invocable = (Invocable) jsEngine;
        try {
            jsEngine.eval(String.format("function test(row){ %s }", script));
            Predicate<LogDataRow> predicate = invocable.getInterface(Predicate.class);
            
            return new LambdaFilter(predicate);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
