//
//  StatsGuessDistribution.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct StatsGuessDistribution: View
{
    @EnvironmentObject private var dimensions: SceneDimensions
    
    var body: some View
    {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension StatsGuessDistribution
{
    struct Row: View
    {
        @EnvironmentObject private var dimensions: SceneDimensions
        
        var round: String
        
        var body: some View
        {
            HStack(spacing: dimensions.width * (5 / idealFrameWidth()))
            {
                Text(round)
                    .font(Font.custom("Roboto-Regular", size: dimensions.height * (20 / idealFrameHeight())))
                    .multilineTextAlignment(.trailing)
            }
            .foregroundColor(.ui.onSurface)
        }
    }
}
