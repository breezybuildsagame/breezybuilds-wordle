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
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var rows: [[GameSceneKeyboard.Key]] = []
    
    var body: some View
    {
        ZStack
        {
            VStack(spacing: sceneDimensions.height * (6.0 / idealFrameHeight()))
            {
                ForEach(rows, id: \.self)
                { row in
                    HStack(spacing: sceneDimensions.width * (4.0 / idealFrameWidth()))
                    {
                        ForEach(row, id: \.self) { keyView in keyView }
                    }
                }
            }
        }
    }
}

extension GameSceneKeyboard
{
    struct Key: View, Hashable, Equatable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var backgroundColor: GameKeyboard.KeyBackgroundColor = .default_
        
        var letters: String? = nil
        
        var resourceId: String? = nil
        
        var onTap: () -> () = { }
        
        static func == (lhs: GameSceneKeyboard.Key, rhs: GameSceneKeyboard.Key) -> Bool
        {
            return lhs.letters == rhs.letters && lhs.resourceId == rhs.resourceId && rhs.backgroundColor == lhs.backgroundColor
        }
        
        func hash(into hasher: inout Hasher) {
            hasher.combine("\(letters ?? "")-\(String(describing: resourceId))")
        }
        
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
            
            let keyWidth = letters?.count ?? 0 > 1 || resourceId != nil ? 52.0 : 33.0
            
            Button(action: { onTap() })
            {
                ZStack
                {
                    backgroundColorView
                    
                    if let letters = letters
                    {
                        Text(letters)
                            .font(
                                Font.custom("Roboto-ExtraBold",
                                size: sceneDimensions.height * (13.0 / idealFrameHeight()))
                            )
                            .foregroundColor(.ui.onBackground)
                    }
                    else if let resourceId = resourceId
                    {
                        Image(resourceId)
                            .resizable()
                            .scaledToFit()
                            .frame(
                                width: sceneDimensions.width * (23.0 / idealFrameWidth()),
                                height: sceneDimensions.width * (23.0 / idealFrameWidth())
                            )
                    }
                }
                .frame(
                    width: sceneDimensions.width * (keyWidth / idealFrameWidth()),
                    height: sceneDimensions.height * (56.0 / idealFrameHeight())
                )
                .cornerRadius(sceneDimensions.width * (4 / idealFrameWidth()))
            }
        }
    }
}
