//
//  GameSceneHeader.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GameSceneHeader: View
{
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var title: String? = nil
    var options: [Option] = []
    
    var body: some View
    {
        VStack
        {
            Spacer()
                .frame(
                    width: sceneDimensions.width * (390.0 / idealFrameWidth()),
                    height: sceneDimensions.height * (46.0 / idealFrameHeight())
                )
            
            HStack
            {
                if let firstOption = options.first { firstOption }
                
                Spacer()
                
                if let title = title
                {
                    Text(title)
                        .font(Font.custom("Roboto-Black", size: sceneDimensions.height * (40.0 / idealFrameHeight())))
                        .foregroundColor(Color.ui.onPrimary)
                }
                
                Spacer()
                
                ForEach(options.filter { $0.resourceId == "game_image_stats_icon" }, id: \.id) { option in option }
            }
            .frame(
                width: sceneDimensions.width * (370.0 / idealFrameWidth()),
                alignment: Alignment.center
            )
            
            Spacer()
            
            Color.ui.error
                .frame(
                    width: sceneDimensions.width * (390.0 / idealFrameWidth()),
                    height: sceneDimensions.height * ( 1.0 / idealFrameHeight())
                )
        }
        .frame(height: sceneDimensions.height * (100.0 / idealFrameHeight()))
    }
    
    struct Option: View, Identifiable
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var id: String = UUID().uuidString
        var resourceId: String? = nil
        var onTap: () -> () = { }
        
        var body: some View
        {
            Button(
                action: { onTap() },
                label:
                { if let resourceId = resourceId
                    {
                        Image(resourceId)
                            .resizable()
                            .scaledToFit()
                            .frame(
                                width: sceneDimensions.width * (24.0 / idealFrameWidth()),
                                height: sceneDimensions.width * (24.0 / idealFrameWidth())
                            )
                    }
                }
            )
        }
    }
}
