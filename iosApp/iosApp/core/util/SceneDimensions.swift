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
    
    func setDimensions(width: CGFloat, height: CGFloat)
    {
        if (self.width != width && self.height != height)
        {
            self.width = width
            self.height = height
            
            dimensionsHaveReset = true
        }
    }
}
