package com.getstream.sdk.chat.logger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StreamChatLogger implements StreamLogger {

    private boolean showLogs;
    private StreamLoggerLevel loggingLevel;

    private StreamChatLogger(boolean showLogs, @Nullable StreamLoggerLevel loggingLevel) {
        this.showLogs = showLogs;
        this.loggingLevel = loggingLevel == null ? StreamLoggerLevel.INFO : loggingLevel;
    }

    public void logI(@NonNull Class<?> classInstance, @NonNull String message) {
        if (showLogs && loggingLevel.isMoreOrEqualsThan(StreamLoggerLevel.INFO)) {
            Log.i(getTag(classInstance), message);
        }
    }

    public void logD(@NonNull Class<?> classInstance, @NonNull String message) {
        if (showLogs && loggingLevel.isMoreOrEqualsThan(StreamLoggerLevel.DEBUG)) {
            Log.d(getTag(classInstance), message);
        }
    }

    public void logW(@NonNull Class<?> classInstance, @NonNull String message) {
        if (showLogs && loggingLevel.isMoreOrEqualsThan(StreamLoggerLevel.WARN)) {
            Log.w(getTag(classInstance), message);
        }
    }

    public void logE(@NonNull Class<?> classInstance, @NonNull String message) {
        if (showLogs && loggingLevel.isMoreOrEqualsThan(StreamLoggerLevel.ERROR)) {
            Log.e(getTag(classInstance), message);
        }
    }

    private String getTag(@NonNull Class<?> classInstance) {
        return classInstance.getSimpleName();
    }

    public static class Builder {

        private boolean showLogs;
        private StreamLoggerLevel loggingLevel;

        /**
         * Enable logs for Build variant.
         * enable(BuildConfig.DEBUG) allow to showing logs only in Debug mode
         *
         * @param variant - Build Variant predicate
         * @return - builder
         */
        public Builder enabled(boolean variant) {
            if (variant) {
                this.showLogs = true;
            }

            return this;
        }

        /**
         * Set logging level
         * @param level - Logging {@link StreamLoggerLevel}
         * @return - builder
         */
        public Builder loggingLevel(StreamLoggerLevel level) {
            this.loggingLevel = level;

            return this;
        }

        public StreamChatLogger build() {
            return new StreamChatLogger(showLogs, loggingLevel);
        }
    }
}
