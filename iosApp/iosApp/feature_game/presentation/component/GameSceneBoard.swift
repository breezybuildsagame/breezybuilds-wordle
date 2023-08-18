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
    
    var rows: [Row]
    
    var body: some View
    {
        VStack(spacing: sceneDimensions.height * (10.0 / idealFrameHeight()))
        {
            ForEach(rows, id: \.self) { row in row }
        }
    }
}

extension GameSceneBoard
{
    struct Tile: View, Equatable, Hashable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var letter: String? = nil
        
        var rowIndex: Int? = nil
        
        var tileIndex: Int? = nil
        
        var state: GameBoard.TileState = .hidden
        
        var body: some View
        {
            CoreTile(
                letter: letter,
                size: 61.0,
                state: CoreTile.State(rawValue: state.name) ?? CoreTile.State.Hidden
            )
        }
        
        static func == (lhs: GameSceneBoard.Tile, rhs: GameSceneBoard.Tile) -> Bool {
            lhs.letter == rhs.letter
            && lhs.rowIndex == rhs.rowIndex
            && lhs.tileIndex == rhs.tileIndex
            && lhs.state == rhs.state
        }
        
        func hash(into hasher: inout Hasher) {
            hasher.combine("\(String(describing: self.letter))_\(UUID())")
        }
    }
}

extension GameSceneBoard
{
    struct Row: View, Hashable, Equatable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var tiles: [GameSceneBoard.Tile] = []
        
        var id = UUID()
        
        var body: some View
        {
            HStack(spacing: sceneDimensions.width * (7.0 / idealFrameWidth()))
            {
                ForEach(0..<tiles.count, id: \.self)
                { tileIndex in
                    GameSceneBoard.Tile(
                        letter: tiles[tileIndex].letter,
                        tileIndex: tileIndex,
                        state: tiles[tileIndex].state
                    )
                }
            }
        }
        
        static func == (lhs: GameSceneBoard.Row, rhs: GameSceneBoard.Row) -> Bool {
            lhs.tiles == rhs.tiles
            && lhs.id == rhs.id
        }
        
        func hash(into hasher: inout Hasher) { hasher.combine(id) }
    }
}
