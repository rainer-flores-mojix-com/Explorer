package com.stratio.crossdata;

import java.util.List;
import java.util.Properties;

import com.nflabs.zeppelin.interpreter.AsyncInterpreterResult;
import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.notebook.Paragraph;
import com.nflabs.zeppelin.scheduler.Job;
import com.nflabs.zeppelin.scheduler.Scheduler;
import com.nflabs.zeppelin.scheduler.SchedulerFactory;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.utils.CrossdataUtils;

public class CrossdataInterpreter extends Interpreter {

    private final BasicDriver xdDriver;
    private Paragraph paragraph;

    static {
        Interpreter.register("xd", CrossdataInterpreter.class.getName());
    }

    public CrossdataInterpreter(Properties property) {
        super(property);
        //Driver that connects to the CROSSDATA servers.
        xdDriver = new BasicDriver();
        xdDriver.setUserName("USER");
    }

    @Override public void open() {
        try {
            connect();
        } catch (ConnectionException e) {
            //            return new InterpreterResult(InterpreterResult.Code.ERROR, "couldn't connect with Crossdata server");
            System.out.println(e.getMessage());
        }

    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        Result result;

        CrossdataResultHandler callback = new CrossdataResultHandler(this, paragraph);

        try {
            result = xdDriver.executeAsyncRawQuery(st.replaceAll("\\s+", " ").trim(), callback);
            if (ErrorResult.class.isInstance(result)) {
                return new InterpreterResult(InterpreterResult.Code.ERROR,
                        ErrorResult.class.cast(result).getErrorMessage());
            } else if (InProgressResult.class.isInstance(result)) {
                return new AsyncInterpreterResult(InterpreterResult.Code.SUCCESS, callback);
            }
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, CrossdataUtils.resultToString(result));

        } catch (Exception e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
        }

    }

    public void removeHandler(String queryId) {
        xdDriver.removeResultHandler(queryId);
    }

    @Override public void cancel() {
        paragraph.setStatus(Job.Status.ABORT);
        paragraph.setListener(null);
        paragraph = null;
    }

    @Override public void bindValue(String name, Object o) {
        if (name.equals("paragraph") && Paragraph.class.isInstance(o)) {
            this.paragraph = Paragraph.class.cast(o);
        }
    }

    @Override public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override public int getProgress() {
        return 0;
    }

    @Override
    public Scheduler getScheduler() {
        return SchedulerFactory.singleton().createOrGetParallelScheduler("interpreter_" + this.hashCode(), 100);
    }

    @Override public List<String> completion(String buf, int cursor) {
        return null;
    }

    /**
     * Establish the connection with the Crossdata servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public void connect() throws ConnectionException {
        xdDriver.connect(xdDriver.getUserName());
    }

}
