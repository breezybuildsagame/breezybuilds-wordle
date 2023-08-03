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
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var title: String
    var instructions: [HelpSheetContent.Instruction]
    
    var body: some View
    {
        ZStack
        {
            VStack
            {
                Text(title)
                    .font(Font.custom("Roboto-Black", size: sceneDimensions.height * (20.0 / idealFrameHeight())))
                    .multilineTextAlignment(.center)
                    .foregroundColor(.ui.onSurface)
                    .frame(
                        width: sceneDimensions.width * (300.0 / idealFrameWidth()),
                        height: sceneDimensions.height * (50.0 / idealFrameHeight())
                    )
                    .padding(sceneDimensions.height * (20.0 / idealFrameHeight()))
                
                ForEach(instructions, id: \.id) { $0 }
                
                Color.ui.error
                    .frame(height: sceneDimensions.height * (1.0 / idealFrameHeight()))
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
            }
        }
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

extension HelpSheetContent
{
    struct Instruction: View, Identifiable, Hashable, Equatable
    {
        var id: UUID = UUID()
        
        static func == (lhs: HelpSheetContent.Instruction, rhs: HelpSheetContent.Instruction) -> Bool
        {
            lhs.instruction == rhs.instruction
        }
        
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var instruction: String
        
        var body: some View
        {
            Text(instruction)
                .font(Font.custom("Roboto-Regular", size: sceneDimensions.height * (15.0 / idealFrameHeight())))
                .foregroundColor(.ui.onSurface)
                .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
        }
        
        func hash(into hasher: inout Hasher) { hasher.combine(id) }
    }
}
