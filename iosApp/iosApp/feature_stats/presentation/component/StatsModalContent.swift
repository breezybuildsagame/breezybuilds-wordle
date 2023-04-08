//
//  StatsModalCotent.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct StatsModalContent: View {
    var body: some View {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension StatsModalContent
{
    struct Stat: View
    {
        @EnvironmentObject private var dimensions: SceneDimensions
        
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
    }
}
