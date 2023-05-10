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
    struct Tile: View, Identifiable, Hashable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var id: UUID = UUID()
        
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
        
        static func == (lhs: HelpSheetContent.Tile, rhs: HelpSheetContent.Tile) -> Bool
        {
            lhs.id == rhs.id && lhs.letter == rhs.letter && lhs.state == rhs.state
        }
        
        func hash(into hasher: inout Hasher) { hasher.combine(id) }
    }
}

extension HelpSheetContent
{
    struct Example: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var tiles: [HelpSheetContent.Tile]
        
        var description: String = ""
        
        var body: some View
        {
            VStack(spacing: sceneDimensions.height * (15.0 / idealFrameHeight()))
            {
                HStack(spacing: sceneDimensions.width * (6.0 / idealFrameWidth()))
                {
                    ForEach(tiles, id: \.self) { $0 }
                    
                    Spacer()
                }
                
                Text(description)
                    .multilineTextAlignment(.leading)
                    .font(
                        Font.custom("Roboto-Regular",
                        size: sceneDimensions.height * (15.0 / idealFrameHeight()))
                    )
                    .foregroundColor(.ui.onBackground)
            }
        }
    }
}
