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
    var examples: [HelpSheetContent.Example]
    var footer: String
    var closeButton: HelpSheetContent.CloseButton
    
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
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
                    .padding(.horizontal, sceneDimensions.width * (25.0 / idealFrameWidth()))
                
                ForEach(instructions, id: \.id) { $0 }
                
                Color.ui.error
                    .frame(height: sceneDimensions.height * (1.0 / idealFrameHeight()))
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
                
                Text("Examples")
                    .font(Font.custom("Roboto-Regular", size: sceneDimensions.height * (16.0 / idealFrameHeight())))
                    .multilineTextAlignment(.leading)
                    .foregroundColor(.ui.onSurface)
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
                
                ForEach(examples, id: \.id) { $0 }
                
                Color.ui.error
                    .frame(height: sceneDimensions.height * (1.0 / idealFrameHeight()))
                    .padding(.top, sceneDimensions.height * (20.0 / idealFrameHeight()))
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
                
                Text(footer)
                    .font(Font.custom("Roboto-Black", size: sceneDimensions.height * (15.0 / idealFrameHeight())))
                    .foregroundColor(.ui.onSurface)
                    .multilineTextAlignment(.leading)
                
                Spacer()
            }
            .frame(
                width: sceneDimensions.width * (350.0 / idealFrameWidth()),
                alignment: .leading
            )
            
            VStack
            {
                closeButton
                
                Spacer()
            }
        }
        .frame(alignment: .top)
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
    struct Example: View, Identifiable, Hashable, Equatable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var id: UUID = UUID()
        
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
                
                HStack
                {
                    Text(description)
                        .multilineTextAlignment(.leading)
                        .font(
                            Font.custom("Roboto-Regular",
                            size: sceneDimensions.height * (15.0 / idealFrameHeight()))
                        )
                        .foregroundColor(.ui.onBackground)
                    
                    Spacer()
                }
            }
        }
        
        static func == (lhs: HelpSheetContent.Example, rhs: HelpSheetContent.Example) -> Bool {
            lhs.tiles == rhs.tiles
            && lhs.description == rhs.description
        }
        
        func hash(into hasher: inout Hasher) { hasher.combine(id) }
    }
}

extension HelpSheetContent
{
    struct Instruction: View, Identifiable, Hashable, Equatable
    {
        var id: UUID = UUID()
        
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var instruction: String
        
        var body: some View
        {
            HStack
            {
                Text(instruction)
                    .font(Font.custom("Roboto-Regular", size: sceneDimensions.height * (15.0 / idealFrameHeight())))
                    .multilineTextAlignment(.leading)
                    .foregroundColor(.ui.onSurface)
                    .padding(.bottom, sceneDimensions.height * (20.0 / idealFrameHeight()))
                Spacer()
            }
        }
        
        static func == (lhs: HelpSheetContent.Instruction, rhs: HelpSheetContent.Instruction) -> Bool
        {
            lhs.instruction == rhs.instruction
        }
        
        func hash(into hasher: inout Hasher) { hasher.combine(id) }
    }
}

extension HelpSheetContent
{
    struct CloseButton: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var imageResourceId: String? = nil
        var onClick: () -> Void = { }
        
        var body: some View
        {
            HStack
            {
                Spacer()
                
                if let imageResourceId = imageResourceId
                {
                    Button(action: onClick)
                    {
                        Image(imageResourceId)
                            .frame(width: sceneDimensions.height * (25.0 / idealFrameHeight()))
                            .frame(height: sceneDimensions.height * (25.0 / idealFrameHeight()))
                    }
                }
            }
            .padding(.top, sceneDimensions.height * (10.0 / idealFrameHeight()))
            .padding(.trailing, sceneDimensions.width * (15.0 / idealFrameWidth()))
        }
    }
}
