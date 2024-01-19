package com.sportsradar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTest {

    @Test
    public void test1(){
        Assertions.assertThat(1).isEqualTo(1);
    }
}
