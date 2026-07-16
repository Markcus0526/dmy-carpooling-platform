//
//  SoundTools.m
//  音效播放
//
//  Created by maguanghai on 14-7-29.
//  Copyright (c) 2014年 maguanghai. All rights reserved.
//

#import "SoundTools.h"
#import <AudioToolbox/AudioToolbox.h>
@interface SoundTools()
@property(nonatomic,strong)NSMutableDictionary *dict;
@end

@implementation SoundTools

singletonImplemention(SoundTools)


- (instancetype)init
{
    self = [super init];
    if (self) {
        [self loadAllSounds];
    }
    return self;
}
- (NSMutableDictionary *)dict
{
    if(_dict ==nil)
    {
        _dict =[NSMutableDictionary dictionary];
    }
    return _dict;
}

-(SystemSoundID)creatSoundIDWithFileName:(NSString *)fileName
{
    SystemSoundID soundID;
    NSURL *url =[[NSBundle mainBundle]URLForResource:fileName withExtension:nil subdirectory:@"Sounds.bundle"];
    AudioServicesCreateSystemSoundID((__bridge CFURLRef)(url), &(soundID));
    
    return soundID;
}
-(void)loadAllSounds
{
    NSString *path =[[NSBundle mainBundle].bundlePath stringByAppendingPathComponent:@"Sounds.bundle"];
    NSArray *soundFiles =[[NSFileManager defaultManager]contentsOfDirectoryAtPath:path error:NULL];
    [soundFiles enumerateObjectsUsingBlock:^(NSString *fileName, NSUInteger idx, BOOL *stop) {
        SystemSoundID soundID= [self creatSoundIDWithFileName:fileName];
        [self.dict setValue:@(soundID )forKey:fileName];
    }];
    //NSLog(@"%@",self.dict);
}
-(void)playSoundWithFileName:(NSString *)fileName
{
    SystemSoundID soundID=[self.dict[fileName] unsignedIntValue];;
    AudioServicesPlaySystemSound(soundID);
    
}

@end
