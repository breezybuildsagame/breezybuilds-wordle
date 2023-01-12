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
        
        var state: GameBoard.TileState = .hidden
        
        var body: some View
        {
            ZStack
            {
                Text("Holla")
            }
            .frame(
                width: sceneDimensions.height * (61.0 / idealFrameHeight()),
                height: sceneDimensions.height * (61.0 / idealFrameHeight())
            )
            .border(Color.black, width: sceneDimensions.height * (2.0 / idealFrameHeight()))
        }
    }
}
