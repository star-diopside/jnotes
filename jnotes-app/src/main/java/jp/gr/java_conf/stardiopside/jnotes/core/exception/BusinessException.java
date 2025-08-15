package jp.gr.java_conf.stardiopside.jnotes.core.exception;

import jp.gr.java_conf.stardiopside.jnotes.core.message.ResultMessage;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final ResultMessage resultMessage;

    public BusinessException(ResultMessage resultMessage) {
        super();
        this.resultMessage = resultMessage;
    }

    public BusinessException(ResultMessage resultMessage, Throwable cause) {
        super(cause);
        this.resultMessage = resultMessage;
    }
}
