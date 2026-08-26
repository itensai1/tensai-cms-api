package com.tensai.cms.telegram.api.commands;

import java.util.List;

public record Keyboard(
        List<List<Button>> buttons
) {
}
