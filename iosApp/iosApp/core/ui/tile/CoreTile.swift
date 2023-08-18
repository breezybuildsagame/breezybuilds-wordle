//
//  CoreTile.swift
//  iosApp
//
//  Created by Bradley Phillips on 5/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct CoreTile: View
{
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var letter: String? = nil
    
    var size: Double
    
    var state: State = .Hidden
    
    var body: some View
    {
        let backgroundColor: Color = {
            switch(state)
            {
                case .Close: return Color.ui.tertiary
                case .Correct: return Color.ui.secondary
                case .Incorrect: return Color.ui.error
                default: return Color.clear
            }
        }()
        
        return ZStack
        {
            if let letter = letter
            {
                Text(letter)
                    .font(Font.custom("Roboto-Bold",
                          size: sceneDimensions.height * (size / idealFrameHeight()) * (35.0 / 61.0))
                    )
                    .foregroundColor(.ui.onPrimary)
            }
        }
        .frame(
            width: sceneDimensions.height * (size / idealFrameHeight()),
            height: sceneDimensions.height * (size / idealFrameHeight())
        )
        .background(backgroundColor)
        .border(
            Color.ui.error,
            width: sceneDimensions.height * (size / idealFrameHeight()) * (state != .Hidden ? 0.0 : 2.0 / 61.0)
        )
    }
    
    static func == (lhs: CoreTile, rhs: CoreTile) -> Bool {
        lhs.letter == rhs.letter
        && lhs.state == rhs.state
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine("\(String(describing: self.letter))_\(UUID())")
    }
    
    enum State: String
    {
        case Close = "CLOSE", Correct = "CORRECT", Hidden = "HIDDEN", Incorrect = "INCORRECT";
    }
}
