//
//  StatsGuessDistribution.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct StatsGuessDistribution: View, Identifiable, Equatable
{
    @EnvironmentObject private var dimensions: SceneDimensions
    
    internal var id: String = UUID().uuidString
    var title: String
    var rows: [StatsGuessDistribution.Row]
    
    var body: some View
    {
        VStack(spacing: dimensions.height * (5 / idealFrameHeight()))
        {
            Text(title)
                .font(Font.custom("Roboto-Black", size: dimensions.height * (20 / idealFrameHeight())))
                .multilineTextAlignment(.center)
                .foregroundColor(Color.ui.onSurface)
                .padding(.bottom, dimensions.height * (20 / idealFrameHeight()))
            
            ForEach(rows, id: \.id) { $0 }
        }
        .padding(.bottom, dimensions.height * (40 / idealFrameHeight()))
    }
    
    static func == (lhs: StatsGuessDistribution, rhs: StatsGuessDistribution) -> Bool
    {
        lhs.title == rhs.title && lhs.rows == rhs.rows
    }
}

extension StatsGuessDistribution
{
    struct Row: View, Identifiable, Equatable
    {
        @EnvironmentObject private var dimensions: SceneDimensions
        
        private(set) var id: String = UUID().uuidString
        var round: String
        var correctGuessCount: String
        
        var body: some View
        {
            HStack( alignment: .center, spacing: dimensions.width * (5 / idealFrameWidth()))
            {
                Text(round)
                    .font(Font.custom("Roboto-Regular", size: dimensions.height * (20 / idealFrameHeight())))
                    .multilineTextAlignment(.trailing)
                
                Text(correctGuessCount)
                    .font(Font.custom("Roboto-Regular", size: dimensions.height * (20 / idealFrameHeight())))
                    .multilineTextAlignment(.trailing)
                    .lineSpacing(dimensions.height * (25 / idealFrameHeight()))
                    .padding(.horizontal, dimensions.width * (5 / idealFrameWidth()))
                    .background(Color.ui.error)
                    
            }
            .foregroundColor(.ui.onSurface)
        }
        
        static func == (lhs: StatsGuessDistribution.Row, rhs: StatsGuessDistribution.Row) -> Bool
        {
            lhs.round == rhs.round && lhs.correctGuessCount == rhs.correctGuessCount
        }
    }
}
