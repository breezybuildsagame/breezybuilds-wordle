//
//  HelpSheet.swift
//  iosApp
//
//  Created by Bradley Phillips on 5/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HelpSheetContent: View
{
    var body: some View
    {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension HelpSheetContent
{
    struct Tile: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var letter: String = ""
        
        var state: HelpSheet.TileState = HelpSheet.TileState.hidden
        
        var body: some View
        {
            CoreTile(
                letter: letter,
                size: 50.0,
                state: CoreTile.State(rawValue: state.name) ?? CoreTile.State.Hidden
            )
        }
    }
}
