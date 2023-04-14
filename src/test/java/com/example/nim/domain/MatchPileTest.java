package com.example.nim.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchPileTest {
    @Test
    public void shouldNotCreateMatchPileWithNegativeAmount() {
        assertThatThrownBy(() -> new MatchPile(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldRemoveMatches() {
        // given
        MatchPile oldMatchPile = new MatchPile(5);

        // when
        MatchPile newMatchPile = oldMatchPile.removeMatch(2);

        // then
        assertThat(newMatchPile).isEqualTo(new MatchPile(3));
    }

    @Test
    public void matchPileWithZeroAmountShouldBeEmpty() {
        assertThat(new MatchPile(0).isEmpty()).isTrue();
    }

}