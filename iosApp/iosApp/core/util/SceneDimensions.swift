//
//  SceneDimensions.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

class SceneDimensions: ObservableObject
{
    static let shared = SceneDimensions()
    
    @Published private (set) var dimensionsHaveReset = false
    
    var width: CGFloat = 0
    var height: CGFloat = 0
    var screenSize: CGSize = .zero
    
    func setDimensions(width: CGFloat, height: CGFloat, screenSize: CGSize = .zero)
    {
        if (self.width != width && self.height != height)
        {
            self.width = width
            self.height = height
            self.screenSize = screenSize
            
            dimensionsHaveReset = true
        }
    }
}
