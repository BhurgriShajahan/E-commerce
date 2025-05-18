package com.auth.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalPlainSerializer extends JsonSerializer<BigDecimal> {
    private static final DecimalFormat df = new DecimalFormat("#.00"); // Format to 2 decimal places
    private static final DecimalFormat zeroDf = new DecimalFormat("0"); // Format for zero without decimals

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            if (value.compareTo(BigDecimal.ZERO) == 0) {
                // If value is zero, write it without decimal places
                gen.writeString(zeroDf.format(value));
            } else {
                // For non-zero values, format with 2 decimal places
                gen.writeString(df.format(value.setScale(2, RoundingMode.HALF_UP)));
            }
        }
    }
}


