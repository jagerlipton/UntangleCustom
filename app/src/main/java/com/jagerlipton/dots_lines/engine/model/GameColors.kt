package com.jagerlipton.dots_lines.engine.model

data class GameColors (var game_untouched_circle_stroke: Int,
                       var game_untouched_circle_fill: Int,
                       var game_touched_circle_stroke: Int,
                       var game_touched_circle_fill: Int,
                       var game_touched_linked_circle_stroke: Int,
                       var game_touched_linked_circle_fill: Int,
                       var game_untouched_lines: Int,
                       var game_touched_lines: Int,
                       var game_bg: Int
)