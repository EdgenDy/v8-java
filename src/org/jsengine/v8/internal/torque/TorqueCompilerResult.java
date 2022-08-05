package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.base.Optional;

import java.util.Vector;

public class TorqueCompilerResult {
	public Optional<SourceFileMap> source_file_map;
	public LanguageServerData language_server_data;
	public Vector<TorqueMessage> messages;
};