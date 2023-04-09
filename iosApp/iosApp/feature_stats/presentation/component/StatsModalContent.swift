//
//  StatsModalCotent.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct StatsModalContent: View
{
    @EnvironmentObject private var dimensions: SceneDimensions
    
    var stats: [StatsModalContent.Stat]
    var guessDistribution: StatsGuessDistribution
    var playAgainButton: StatsModalContent.PlayAgainButton
    
    var body: some View
    {
        VStack
        {
            Text("STATISTICS")
                .font(Font.custom(
                    "Roboto-Black",
                    size: dimensions.height * (20 / idealFrameHeight())
                ))
                .foregroundColor(.ui.onSurface)
                .padding(.top, dimensions.height * (40 / idealFrameHeight()))
            
            HStack(spacing: dimensions.width * (5 / idealFrameWidth()))
            {
                ForEach(stats, id: \.id) { $0 }
            }
            .padding(.bottom, dimensions.height * (15 / idealFrameHeight()))
            
            guessDistribution
            
            playAgainButton
        }
        .frame(width: dimensions.width * (315.0 / idealFrameWidth()))
        .padding(.horizontal, dimensions.width * (20.0 / idealFrameWidth()))
        .cornerRadius(dimensions.height * (8 / idealFrameHeight()))
        .background(Color.ui.surface)
    }
}

extension StatsModalContent
{
    struct Stat: View, Identifiable, Equatable
    {
        @EnvironmentObject private var dimensions: SceneDimensions
        
        internal var id: String = UUID().uuidString
        var headline: String
        var description: String
        
        var body: some View
        {
            VStack
            {
                Text(headline)
                    .font(Font.custom(
                        "Roboto-Regular",
                        size: dimensions.height * (40 / idealFrameHeight())
                    ))
                    .lineSpacing(dimensions.height * (47 / idealFrameHeight()))
                    .multilineTextAlignment(.center)
                    .foregroundColor(.ui.onSurface)
                
                Text(description)
                    .font(Font.custom(
                        "Roboto-Regular",
                        size: dimensions.height * (14 / idealFrameHeight())
                    ))
                    .multilineTextAlignment(.center)
                    .foregroundColor(.ui.onSurface)
            }
            .padding(.bottom, dimensions.height * (15 / idealFrameHeight()))
        }
        
        static func == (lhs: StatsModalContent.Stat, rhs: StatsModalContent.Stat) -> Bool
        {
            lhs.headline == rhs.headline && lhs.description == rhs.description
        }
    }
}

extension StatsModalContent
{
    struct PlayAgainButton: View, Identifiable, Equatable
    {
        @EnvironmentObject private var dimensions: SceneDimensions
        
        internal var id: String = UUID().uuidString
        var label: String
        var tapped: () -> Void
        
        var body: some View
        {
            Button(action: tapped)
            {
                Text(label)
                    .font(Font.custom(
                        "Roboto-Black",
                        size: dimensions.height * (14 / idealFrameHeight())
                    ))
                    .multilineTextAlignment(.center)
                    .foregroundColor(.ui.surface)
            }
            .frame(
                width: dimensions.width * (375 / idealFrameWidth()),
                height: dimensions.height * (50 / idealFrameHeight())
            )
            .background(Color.ui.secondary)
            .cornerRadius(dimensions.width * (4 / idealFrameWidth()))
        }
        
        static func == (lhs: StatsModalContent.PlayAgainButton, rhs: StatsModalContent.PlayAgainButton) -> Bool
        {
            lhs.label == rhs.label
        }
    }
}
