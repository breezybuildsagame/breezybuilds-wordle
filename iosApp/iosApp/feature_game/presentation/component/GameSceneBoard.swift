//
//  GameSceneBoard.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct GameSceneBoard: View
{
    var body: some View
    {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension GameSceneBoard
{
    struct Tile: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var letter: String? = nil
        
        var state: GameBoard.TileState = .hidden
        
        var body: some View
        {
            let backgroundColor: Color = {
                switch(state)
                {
                    case .incorrect: return Color.ui.error
                    default: return Color.clear
                }
            }()
            
            return ZStack
            {
                if let letter = letter
                {
                    Text(letter)
                        .font(Font.custom("Roboto-Bold", size: sceneDimensions.height * (35.0 / idealFrameHeight())))
                        .foregroundColor(.ui.onPrimary)
                }
            }
            .frame(
                width: sceneDimensions.height * (61.0 / idealFrameHeight()),
                height: sceneDimensions.height * (61.0 / idealFrameHeight())
            )
            .background(backgroundColor)
            .border(
                Color.black,
                width: sceneDimensions.height * (state != .hidden ? 0.0 : 2.0 / idealFrameHeight())
            )
        }
    }
}
