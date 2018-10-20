package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResBPError {
    @SerializedName("code")
    Long code;

    @SerializedName("message")
    String message;

    @SerializedName("error")
    String error;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    class Error {
        @SerializedName("code")
        Long code;

        @SerializedName("exception")
        String exception;

        @SerializedName("unspecified")
        String unspecified;

        @SerializedName("details")
        ArrayList<Detail> details;

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getUnspecified() {
            return unspecified;
        }

        public void setUnspecified(String unspecified) {
            this.unspecified = unspecified;
        }

        public ArrayList<Detail> getDetails() {
            return details;
        }

        public void setDetails(ArrayList<Detail> details) {
            this.details = details;
        }

        class Detail {
            @SerializedName("message")
            String message;
            @SerializedName("file")
            String file;
            @SerializedName("line_number")
            Long line_number;
            @SerializedName("method")
            String method;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public Long getLine_number() {
                return line_number;
            }

            public void setLine_number(Long line_number) {
                this.line_number = line_number;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }
        }
    }
}
