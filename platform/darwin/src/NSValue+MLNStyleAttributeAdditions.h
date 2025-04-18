#import <Foundation/Foundation.h>

#include <array>

@interface NSValue (MLNStyleAttributeAdditions)

+ (instancetype)mgl_valueWithOffsetArray:(std::array<float, 2>)offsetArray;
+ (instancetype)mgl_valueWithPaddingArray:(std::array<float, 4>)paddingArray;
+ (instancetype)mgl_valueWithLocationArray:(std::array<double, 3>)positionArray;

- (std::array<float, 2>)mgl_offsetArrayValue;
- (std::array<float, 4>)mgl_paddingArrayValue;
- (std::array<double, 3>)mgl_locationArrayValue;
- (std::array<float, 3>)mgl_lightPositionArrayValue;

@end
