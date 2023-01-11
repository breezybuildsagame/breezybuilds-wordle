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
                
                if let title = title
                {
                    Text(title)
                        .font(Font.system(size: sceneDimensions.height * (40.0 / idealFrameHeight())))
                        .foregroundColor(Color.ui.onPrimary)
                }
            }
        }
    }
    
    struct Option: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
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
