//
//  GameSceneAnnouncement.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/18/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GameSceneAnnouncement: View
{
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var text: String
    
    var body: some View
    {
        let shadowOffset = sceneDimensions.height * (4.0 / idealFrameHeight())
        
        return Text(text)
            .font(Font.custom("Roboto-Bold", size: sceneDimensions.height * (20.0 / idealFrameHeight())))
            .padding(.all, sceneDimensions.height * (26.0 / idealFrameHeight()))
            .foregroundColor(Color.ui.primary)
            .background(
                Color.ui.onPrimary
                    .cornerRadius(sceneDimensions.height * (8.0 / idealFrameHeight()))
                    .shadow(color: Color(red: 0, green: 0, blue: 0, opacity: 1), radius: shadowOffset, y: shadowOffset)
            )
    }
}
