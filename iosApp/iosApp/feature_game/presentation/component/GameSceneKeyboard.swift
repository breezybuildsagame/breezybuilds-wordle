//
//  GameSceneKeyboard.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/15/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct GameSceneKeyboard: View
{
    var body: some View
    {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension GameSceneKeyboard
{
    struct Key: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var backgroundColor: GameKeyboard.KeyBackgroundColor = .default_
        
        var body: some View
        {
            let backgroundColorView: Color =
            {
                switch(backgroundColor)
                {
                    case .correct: return .ui.secondary
                    case .nearby: return .ui.tertiary
                    case .notFound: return .ui.error
                    default: return .ui.background
                }
            }()
            
            ZStack
            {
                backgroundColorView
            }
            .frame(
                width: sceneDimensions.width * (33.0 / idealFrameWidth()),
                height: sceneDimensions.height * (56.0 / idealFrameHeight())
            )
            .cornerRadius(sceneDimensions.width * (4 / idealFrameWidth()))
        }
    }
}
