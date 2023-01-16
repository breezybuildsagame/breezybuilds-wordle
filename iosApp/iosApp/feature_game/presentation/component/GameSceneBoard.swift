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
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var rows: [[GameBoard.Tile]] = []
    
    var body: some View
    {
        VStack(spacing: sceneDimensions.height * (10.0 / idealFrameHeight()))
        {
            ForEach(rows, id: \.self)
            { row in
                HStack(spacing: sceneDimensions.width * (7.0 / idealFrameWidth()))
                {
                    ForEach(0..<row.count, id: \.self)
                    { tileIndex in
                        GameSceneBoard.Tile(letter: row[tileIndex].letter() as? String)
                    }
                }
            }
        }
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
                    case .close: return Color.ui.tertiary
                    case .correct: return Color.ui.secondary
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
                Color.ui.error,
                width: sceneDimensions.height * (state != .hidden ? 0.0 : 2.0 / idealFrameHeight())
            )
        }
    }
}
